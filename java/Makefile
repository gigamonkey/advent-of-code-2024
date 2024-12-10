SHELL		:= bash -O globstar
MAIN_CLASS	:= com.gigamonkeys.aoc2024.AdventOfCode

sources = $(shell ls src/**/*.java)
run = java -cp classes $(MAIN_CLASS)

today: build
	$(run)

all: build run

run:
	$(run) --all


inputs/day-%:
	mkdir -p $@
	touch $@/part-1-test.expected
	#touch $@/part-1.expected
	touch $@/part-2-test.expected
	#touch $@/part-2.expected
	touch $@/real.txt
	touch $@/test.txt


build: | classes
	javac -d classes -Xdiags:verbose -Xlint:all $(sources)

classes:
	mkdir $@

pretty:
	prettier -w $(sources)

clean:
	rm -rf classes
