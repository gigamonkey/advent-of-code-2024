SHELL		:= bash -O globstar
MAIN_CLASS	:= com.gigamonkeys.aoc2024.AdventOfCode
DAY		:= 1
PART		:= 2

all: build run

run:
	java -cp classes $(MAIN_CLASS) $(DAY) $(PART)

build: | classes
	javac -d classes -Xdiags:verbose -Xlint:all $(shell ls src/**/*.java)

classes:
	mkdir $@

clean:
	rm -rf classes
