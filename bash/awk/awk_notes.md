awk is a pattern matching language, matches data based on regular expression & then performs an action based on the data




basic structure of awk program

pattern { action }

awk '{ print }' test.txt   ## Return all lines w/ awk
awk '{ print $1 }' test.txt ### Return column 1 of all lines
awk '{ print $1, $2 }' test.txt ### Return column 1 & column 2 separated by a space of all lines
awk '{ print $1.$2 }' test.txt ### Return column 1 & column 2 concatenated of all lines


awk '/test/ { print }' test.txt  ## return all lines that contain test (case sensitive) 

awk '/[a-z]/ { print } ' test.txt ## Return all lines that contain at least a single lower case letter
awk '/^[0-9]/ { print }' test.txt  ## Return all lines that contain a number

awk '{ if($1 ~ /123/) print }' test.txt

## All lines that ened with a number using the $

awk '/[0-9]$/ { print } ' test.txt

## Print lines whose first column is 123.
awk '{ if($1 ~ /123/)  print }' test.txt -

## Pipe outputs 
grep -i test test.txt | awk '/[0-9]/ { print }'

## Override the delimiter w/ -F flat
awk -F: '{ print $2 }' test.txt