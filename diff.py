
def compare(lst1,lst2):
    if len(lst1)!=len(lst2):
        return False
    else:
        for R in lst1:

            if R in lst2:
                continue
            else:
                return False
        return True

import os

# folder path
dir_path_output = "./data/minimization/output"
dir_path_expected_output = "./data/minimization/expected_output"
count_output=0
count_expected_output=0
for path in os.listdir(dir_path_output):
    # check if current path is a file
    if os.path.isfile(os.path.join(dir_path_output, path)):
        count_output += 1

for path in os.listdir(dir_path_expected_output):
    # check if current path is a file
    if os.path.isfile(os.path.join(dir_path_output, path)):
        count_expected_output += 1

if(count_output!=count_expected_output):
    raise ValueError("The number of output files doesn't match the number of expected_output files!")

       
for i in range(count_expected_output):
    lst_output=[]
    lst_expected_output=[]
    with open('./data/minimization/output/query' + str(i+1) + '.txt', 'r') as f:
        output = f.readline().strip()
        lst_output=output.split(':-')[1:][0].split(',')
    with open('./data/minimization/expected_output/query' + str(i+1) + '.txt', 'r') as f:
        expected_output = f.readline().strip()
        lst_expected_output=expected_output.split(':-')[1:][0].split(',')
    
    check=compare(lst_expected_output,lst_output)
    if check:
        print("query{0}.txt is correct".format(i+1))
    else:
        print("query{0}.txt is wrong".format(i+1))




