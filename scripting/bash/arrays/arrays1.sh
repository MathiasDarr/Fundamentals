#! /bin/bash
Unix[0]='Debian'
declare -a Unix=('Debian' 'Red hat' 'Ubuntu' 'Suse' 'Fedora');

echo ${Unix[@]}

echo "The number of elements in the array is " ${#Unix[@]}
echo "Number of characters in the first element of the array " ${#Unix}
echo "Number of characters in the 3rd element of the array " ${#Unix[3]}