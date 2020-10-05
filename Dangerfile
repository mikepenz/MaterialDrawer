github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet.
has_wip_label = github.pr_labels.any? { |label| label.include? "Engineers at work" }
has_wip_title = github.pr_title.include? "[WIP]"

if has_wip_label || has_wip_title
	warn("PR is marked as Work in Progress")
end

# Ensure the PR is not marked as DO NOT MERGE
fail("PR specifies label DO NOT MERGE") if github.pr_labels.any? { |label| label.include? "DO NOT MERGE" }

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 5000

File.open("settings.gradle", "r") do |file_handle|
  file_handle.each_line do |setting|
    if setting.include? "include"
        gradleModule = setting[10, setting.length-12]

        # AndroidLint
        androidLintFile = String.new(gradleModule + "/build/reports/lint-results.xml")
        androidLintDebugFile = String.new(gradleModule + "/build/reports/lint-results-debug.xml")
        if File.file?(androidLintFile) || File.file?(androidLintDebugFile)
            android_lint.skip_gradle_task = true
            android_lint.severity = "Warning"
            if File.file?(androidLintFile)
                android_lint.report_file = androidLintFile
            else
                android_lint.report_file = androidLintDebugFile
            end
            android_lint.filtering = true
            android_lint.lint(inline_mode: true)
        end

        # Detekt
        detektFile = String.new(gradleModule + "/build/reports/detekt.xml")
        if File.file?(detektFile)
            kotlin_detekt.report_file = detektFile
            kotlin_detekt.skip_gradle_task = true
            kotlin_detekt.severity = "warning"
            kotlin_detekt.filtering = true
            kotlin_detekt.detekt(inline_mode: true)
        end
    end
  end
end