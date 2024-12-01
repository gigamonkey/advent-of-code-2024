SHELL		:= bash -O globstar
USERNAME	:= peterseibel
MAIN_CLASS	:= net.berkeley.students.$(USERNAME).AdventOfCode

all: build run

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
