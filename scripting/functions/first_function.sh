#!/bin/bash

function hello {
  echo Hello!
}

function f1 {
  echo hello $1
}

hello
f1 faa
