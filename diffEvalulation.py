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
        
        # read the contents of each file into a list of lists
        csv1 = list(csv.reader(file1))
        csv2 = list(csv.reader(file2))
        
        # compare the two lists of lists to check if they are the same
        if csv1 == csv2:
            print(f"The contents of {filename} passes.")
        else:
            print(f"The contents of {filename} fails.")
            
        # close the files
        file1.close()
        file2.close()
    else:
        print(f"{filename} does not exist in {folder2}.")
