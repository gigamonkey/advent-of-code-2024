SHELL		:= bash -O globstar
MAIN_CLASS	:= com.gigamonkeys.aoc2024.AdventOfCode

sources = $(shell ls src/**/*.java)

all: build run

run:
	java -cp classes $(MAIN_CLASS) --all

build: | classes
	javac -d classes -Xdiags:verbose -Xlint:all $(sources)

classes:
	mkdir $@

pretty:
	prettier -w $(sources)

clean:
	rm -rf classes
