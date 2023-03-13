package ed.inf.adbs.minibase.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestUlits {
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
}
