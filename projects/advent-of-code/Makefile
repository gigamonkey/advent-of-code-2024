SHELL		:= bash -O globstar
USERNAME	:= $(error Set the USERNAME variable in the Makefile to the part of your student email before the @)
MAIN_CLASS	:= net.berkeley.students.$(USERNAME).AdventOfCode

all: build

run:
	java -cp classes $(MAIN_CLASS)

build: | classes
	javac -d classes -Xdiags:verbose -Xlint:all $(shell ls src/**/*.java)

classes:
	mkdir $@

clean:
	rm -rf classes

init: src/net/berkeley/students/$(USERNAME)/AdventOfCode.java

src/net/berkeley/students/$(USERNAME)/AdventOfCode.java: | src/net/berkeley/students/$(USERNAME)
	echo "package net.berkeley.students.$(USERNAME);" > $@
	echo "" >> $@
	echo "public class $$(basename $@ .java) {" >> $@
	echo "  public static void main(String[] args) {" >> $@
	echo "    System.out.println(\"Welcome to Advent of Code!\");" >> $@
	echo "  }" >> $@
	echo "}" >> $@

src/net/berkeley/students/$(USERNAME):
	mkdir -p $@
