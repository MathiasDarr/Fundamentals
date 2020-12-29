#!/bin/bash

awk '{if($4 == "") print "Not all scores are available for", $1;}' sample_input.txt