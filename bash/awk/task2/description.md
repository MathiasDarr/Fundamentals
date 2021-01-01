## Task 2

Determine if row's columsn are all greater than 50

awk '{  
if ($2 >=50 ) 
    print $1;
else 
    print $1,":","Fail";
}' input.txt
