## Sed notes
sed = stream editor

Similar to awk, sed is a pattern matching engine that can perform manipulations on lines of text


s - substitute

# Substitute every t w/ 4 o's  
sed 's/t/0000/g' input.txt      


### Sub every line which starts w/ a t using the ^
sed 's/^t/oooo/g' input.txt 

### Sub every t which ends a line w/ use of the $ 
sed 's/t$/k/g' input.txt


### Replace every digit w/ an *

sed 's/[0-9]/*/g' input.txt

### Substitute w/ a the ampersand &

sed 's/[0-9]//g' input.txt

### Substitute every occurence of two digits & surround w/ parenthesis
sed 's/[0-9][0-9]/(&)/g' input.txt

### Substitute w/ *.  sub every digit, or every pair of digits
sed 's/[0-9][0-9]*/{&}/g' input.txt

