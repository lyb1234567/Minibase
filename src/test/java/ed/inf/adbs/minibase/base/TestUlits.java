package ed.inf.adbs.minibase.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TestUlits {
    /**

     Compares the content of two files line by line, where each line consists of comma-separated values.

     @param filename1 The path of the first file to compare.

     @param filename2 The path of the second file to compare.

     @return true if the two files contain the same comma-separated values in each line, false otherwise.

     @throws NullPointerException if either filename1 or filename2 is null.
     */
    public static boolean compareFile(String filename1,String filename2)
    {
        try {
            BufferedReader file1 = new BufferedReader(new FileReader(filename1));
            BufferedReader file2 = new BufferedReader(new FileReader(filename2));

            String [] line1 = file1.readLine().split(",");
            String [] line2 = file2.readLine().split(",");

            if (line1.length!=line2.length)
            {
                System.out.println("The Output file content is:"+file1.readLine());
                System.out.println("The Compare file content is:"+file2.readLine());
                return false;
            }
            for(int i=0;i<line1.length;i++)
            {
                String str1=line1[i].trim();
                String str2=line2[i].trim();
                if (!str1.equals(str2))
                {
                    return false;
                }
            }
            file1.close();
            file2.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
            return false;
        }
    }

    /**

     Compares two files, ignoring the order of lines in the files.

     @param file1 The path of the first file to compare.

     @param file2 The path of the second file to compare.

     @return true if the two files contain the same lines regardless of order, false otherwise.

     @throws NullPointerException if either file1 or file2 is null.
     */
    public static boolean compareFileDifferentOrder(String file1, String file2)
    {
        try (BufferedReader file1Reader = new BufferedReader(new FileReader(file1));
             BufferedReader file2Reader = new BufferedReader(new FileReader(file2))) {
            String[] file1Lines = file1Reader.lines().toArray(String[]::new);
            String[] file2Lines = file2Reader.lines().toArray(String[]::new);


            Arrays.sort(file1Lines);
            Arrays.sort(file2Lines);

            if (Arrays.equals(file1Lines, file2Lines)) {
                return true;
            }
            else
            {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
