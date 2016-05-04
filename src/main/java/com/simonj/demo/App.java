package com.simonj.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.testng.util.Strings;

import com.clearspring.analytics.util.Preconditions;
import com.google.common.collect.ImmutableList;

import scala.Tuple2;

/**
 * Hello world!
 *
 */
public class App {

    public static Map<String, Integer> countWords(List<String> lines) {
        // Be quiet already!
        Logger.getLogger("org").setLevel(Level.WARN);
        Logger.getLogger("akka").setLevel(Level.WARN);

        Map<String, Integer> counts;
        try (JavaSparkContext context = new JavaSparkContext("local[2]", "Spark fun!")) {
            JavaRDD<String> linesAsRDD = context.parallelize(lines).cache();
            JavaRDD<String> words = linesAsRDD.flatMap(line -> Arrays.asList(
                    line.toLowerCase().replaceAll("[^A-Za-z0-9]", " ").replaceAll("( )+", " ").split(" ")));

            counts = words.mapToPair(w -> new Tuple2<String, Integer>(w, 1))
                    .reduceByKey((x, y) -> x + y)
                    .collectAsMap();
            context.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return counts;
    }

    public static void main(String[] args) {
        Preconditions.checkArgument(args.length > 0 && !Strings.isNullOrEmpty(args[0]), "Missing a filename argument.");
        File file = new File(args[0]);
        Preconditions.checkState(file != null && file.exists(), "The File does not exist!");
        List<String> lines = ImmutableList.<String>of();
        try {
            lines = Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (IOException e) {
            Preconditions.checkState(false, "Unable to read the file.");
        }

        System.out.println(countWords(lines));
    }
}
