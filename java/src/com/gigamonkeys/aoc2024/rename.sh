#!/usr/bin/env bash

from=$1
to=$2

perl -pi -e "s/$from/$to/g;" *.java
git mv $from.java $to.java
