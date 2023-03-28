#!/bin/bash

echo "Test Minimization:"
for i in {1..16}
do
  java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query${i}.txt data/minimization/output/query${i}.txt
done
python diffMinimization.py

echo -e "\n\nTest Evaluation:"
for i in {1..20}
do
  java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query${i}.txt data/evaluation/output/query${i}.csv
done
python diffEvalulation.py
