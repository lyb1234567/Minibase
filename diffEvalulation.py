import os
import csv
folder1="./data/evaluation/output"
folder2="./data/evaluation/expected_output"

files1 = os.listdir(folder1)
files2 = os.listdir(folder2)

files1= sorted(files1, key=lambda x: int(os.path.splitext(x)[0].split('query')[1]))
files2= sorted(files2, key=lambda x: int(os.path.splitext(x)[0].split('query')[1]))

for filename in files1:
    # check if the file exists in the other folder
    if filename in files2:
        # open the two CSV files for comparison
        file1 = open(os.path.join(folder1, filename))
        file2 = open(os.path.join(folder2, filename))
        
        file1_lines = file1.readlines()
        file2_lines = file2.readlines()

        file1_lines = [line.strip() for line in file1_lines]
        file2_lines = [line.strip() for line in file2_lines]
        
        if(sorted(file1_lines)==sorted(file2_lines)):
            print("{0} passes".format(filename))
    else:
        print("Expected output do not have {0} !!!".format(filename))
        
        

