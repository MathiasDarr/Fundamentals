#!/bin/bash
input="countries.txt"
# IFS= prevents leading/trailing whitespace from being trimmed
# -r prevents backslash escapes from being interpreted
i=0
while IFS= read -r line
do
  array[$i]=$line
  ((i+=1))
done < "$input"
echo ${array[@]}

