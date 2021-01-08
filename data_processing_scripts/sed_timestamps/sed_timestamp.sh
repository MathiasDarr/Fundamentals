#!/bin/bash

for filename in unprocessed_data/*; do
  fname=$(echo ${filename} | awk -F/ '{print $NF}')
  new_fname='processed_data/'${fname}
  echo ${new_fname}
  sed -E 's/ PM| AM//' ${filename} > ${new_fname}
done


#sed -E 's/ PM| AM//' data/1.json > data/edited.json
