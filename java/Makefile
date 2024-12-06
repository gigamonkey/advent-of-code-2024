SHELL		:= bash -O globstar
MAIN_CLASS	:= com.gigamonkeys.aoc2024.AdventOfCode

sources = $(shell ls src/**/*.java)
run = java -cp classes $(MAIN_CLASS)

today: build
	$(run)

all: build run

run:
	$(run) --all

build: | classes
	javac -d classes -Xdiags:verbose -Xlint:all $(sources)

classes:
	mkdir $@

pretty:
	prettier -w $(sources)

clean:
	rm -rf classes
