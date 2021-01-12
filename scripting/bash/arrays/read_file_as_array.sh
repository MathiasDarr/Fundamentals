#!/bin/bash
input="countries.txt"
i=0
while read line
do
array[$i]=$line
((i+=1))
done

echo ${array[@]}
