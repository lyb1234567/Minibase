@echo off
echo Test Minimization:
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query1.txt data/minimization/output/query1.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query2.txt data/minimization/output/query2.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query3.txt data/minimization/output/query3.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query4.txt data/minimization/output/query4.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query5.txt data/minimization/output/query5.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query6.txt data/minimization/output/query6.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query7.txt data/minimization/output/query7.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query8.txt data/minimization/output/query8.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query9.txt data/minimization/output/query9.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query10.txt data/minimization/output/query10.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query11.txt data/minimization/output/query11.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query12.txt data/minimization/output/query12.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query13.txt data/minimization/output/query13.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query14.txt data/minimization/output/query14.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query15.txt data/minimization/output/query15.txt
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.CQMinimizer data/minimization/input/query16.txt data/minimization/output/query16.txt
python diffMinimization.py

echo.&echo.
echo Test Evaluation:
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query1.txt data/evaluation/output/query1.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query2.txt data/evaluation/output/query2.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query3.txt data/evaluation/output/query3.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query4.txt data/evaluation/output/query4.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query5.txt data/evaluation/output/query5.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query6.txt data/evaluation/output/query6.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query7.txt data/evaluation/output/query7.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query8.txt data/evaluation/output/query8.csv 
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query9.txt data/evaluation/output/query9.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query10.txt data/evaluation/output/query10.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query11.txt data/evaluation/output/query11.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query12.txt data/evaluation/output/query12.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query13.txt data/evaluation/output/query13.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query14.txt data/evaluation/output/query14.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query15.txt data/evaluation/output/query15.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query16.txt data/evaluation/output/query16.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query17.txt data/evaluation/output/query17.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query18.txt data/evaluation/output/query18.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query19.txt data/evaluation/output/query19.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query20.txt data/evaluation/output/query20.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query21.txt data/evaluation/output/query21.csv
java -cp target/minibase-1.0.0-jar-with-dependencies.jar ed.inf.adbs.minibase.Minibase data/evaluation/db data/evaluation/input/query22.txt data/evaluation/output/query22.csv
python diffEvalulation.py