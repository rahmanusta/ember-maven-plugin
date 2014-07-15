ember-maven-plugin
==================

Compiles Ember templates and combines them single JavaScript source as output

## Usage

```xml
<plugin>
    <groupId>com.kodcu</groupId>
    <artifactId>ember-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <templatePath>${project.basedir}/src/main/resources/templates</templatePath>
        <outputPath>${project.basedir}/src/main/resources/templates/output.js</outputPath>
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>precompile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Repository

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/rahmanusta/maven</url>
</repository>
```

## Requirements

* Needs Java 8+

[![Analytics](https://ga-beacon.appspot.com/UA-52823012-1/ember-maven-plugin/readme)](https://github.com/rahmanusta/ember-maven-plugin)
