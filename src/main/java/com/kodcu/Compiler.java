package com.kodcu;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Created by usta on 15.07.2014.
 */
@Mojo(name = "precompile")
public class Compiler extends AbstractMojo {

    @Parameter
    private String templatePath;

    @Parameter
    private String outputPath;

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
    private StringBuffer templateBuffer = new StringBuffer();
    private PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:{**.handlebars,**.hbs}");


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            startExecution();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws ScriptException, IOException, URISyntaxException {
        new Compiler().startExecution();
    }

    private void startExecution() throws ScriptException, URISyntaxException, IOException {

        engine.eval(IOHelper.toString("/node_modules/handlebars/dist/handlebars.min.js"));
        engine.eval("var exports = {}");
        engine.eval(IOHelper.toString("/node_modules/ember-template-compiler/vendor/ember-template-compiler.js"));

        getLog().info("Template path: "+templatePath);
        getLog().info("Output path: "+outputPath);

        Path templatePathh = Paths.get(templatePath);
        Path outputPathh = Paths.get(outputPath);

        Stream<Path> templateStream = Files.walk(templatePathh);

        getLog().info("...Started Ember Template Compiling...");
        templateStream
                .filter(path -> !Files.isDirectory(path))
                .filter(path -> matcher.matches(path))
                .forEach(path -> {
                    getLog().info("Compiling: " + path);
                    String template = new String(IOHelper.readAllBytes(path), Charset.forName("UTF-8"));
                    template = template.replace("\n", "").replace("\r", "").replace("\"", "'");
                    BiConsumer<String, Path> combiner = this::combine;
                    combiner.accept(template, path);
                });


        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputPathh);) {
            bufferedWriter.write(templateBuffer.toString());
        }

        getLog().info("...Completed Ember Template Compiling...");
    }

    public void combine(String template, Path path) {
        try {
            String compiled = engine.eval(String.format("exports.precompile(\"%s\")", template)).toString();
            String embered = String.format("Ember.TEMPLATES['%s'] = Ember.Handlebars.template(%s);", path.getFileName().toString().split("\\.")[0], compiled);
            templateBuffer.append(embered);
            templateBuffer.append("\n");
        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }
}
