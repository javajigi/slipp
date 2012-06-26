call mvn install:install-file  -Dfile=org.eclipse.mylyn.wikitext.core_1.7.0.I20120625-0903.jar -DgroupId=org.eclipse.mylyn.wikitext -DartifactId=core -Dversion=1.7.0.I20120625-0903 -Dpackaging=jar

call mvn install:install-file -DgeneratePom=false -Dfile=org.eclipse.mylyn.wikitext.confluence.core_1.7.0.I20120625-0903-source.jar -DgroupId=org.eclipse.mylyn.wikitext -DartifactId=core -Dversion=1.7.0.I20120625-0903 -Dpackaging=java-source

call mvn install:install-file  -Dfile=org.eclipse.mylyn.wikitext.confluence.core_1.7.0.I20120625-0903.jar -DgroupId=org.eclipse.mylyn.wikitext -DartifactId=confluence -Dversion=1.7.0.I20120625-0903 -Dpackaging=jar

call mvn install:install-file -DgeneratePom=false -Dfile=org.eclipse.mylyn.wikitext.confluence.core_1.7.0.I20120625-0903-source.jar -DgroupId=org.eclipse.mylyn.wikitext -DartifactId=confluence -Dversion=1.7.0.I20120625-0903 -Dpackaging=java-source