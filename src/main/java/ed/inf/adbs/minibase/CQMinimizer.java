package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;
import sun.font.TrueTypeFont;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.io.FileReader;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Minimization of conjunctive queries
 *
 */
public class CQMinimizer {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: CQMinimizer input_file output_file");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];

        minimizeCQ(inputFile, outputFile);

        parsingExample(inputFile);
    }
    public static boolean check_map_contains(int target, HashMap<Integer,HashMap<Integer,List<Term>>> extra)
    {
        return extra.containsKey(target);
    }



//    check if a RelationAtom object is contained in the body
    public static  List<String> check_term_contains(String target, RelationalAtom atom)
    {
        List <String> return_val=new ArrayList<>();
        List<Term> terms=atom.getTerms();
        for (int i=0;i<terms.size();i++)
        {
            if (terms.get(i).toString().equals(target))
            {

                return_val.add("true");
                return_val.add(Integer.toString(i));
                return return_val;
            }
        }
        return_val.add("false");
        return_val.add("12312123123");
        return return_val;
    }
    public  static  boolean is_distinguished(List<Atom> body, List<Variable> variables, String check_value)
    {
        int cnt=0;
        List <String > check = new ArrayList<>();
        for (int i=0;i<variables.size();i++)
        {
            check.add(variables.get(i).toString());
        }
        if (check.contains(check_value))
        {
            for (int i=0;i<body.size();i++)
            {
                RelationalAtom atom =(RelationalAtom) body.get(i);
                for(int j=0;j<atom.getTerms().size();j++)
                {
                    if (atom.getTerms().get(j).toString().equals(check_value))
                    {
                        cnt=cnt+1;
                    }
                }
            }
        }
        return cnt==1;
    }
    public static String Minimized(Head head, List<Atom> body)
    {

        String result= head + " :- ";
        for(int i=0;i<body.size();i++)
        {
            if (i==0)
            {
                result = result + body.get(i);
                continue;
            }
           else
            {
                result=result + ", " + body.get(i);
            }
        }
        return result;
    }
    public static boolean if_equal(List<String> lst1, List<Term>lst2)
    {
        for (int i=0;i<lst1.size();i++)
        {
            if (!lst1.get(i).equals(lst2.get(i).toString()))
            {
                return false;
            }
        }
        return true;
    }
    public static HashMap<String,List<Term>> merge_homo(List<HashMap<Term,Term>> homo)
    {
        HashSet<String> key_set=new HashSet<String>();
        // a hashset for stroing all of the keys
        for (int i=0;i<homo.size();i++)
        {
            for (Map.Entry<Term, Term> set :
                    homo.get(i).entrySet())
            {
                key_set.add(set.getKey().toString());
            }

        }
        Iterator<String> it = key_set.iterator();
        HashMap<String,List<Term>> merged= new HashMap<String,List<Term>>();
        while(it.hasNext())
        {
            String key=it.next();
            List<Term> term_lst=new ArrayList<>();
            for(int i=0;i<homo.size();i++)
            {
                HashMap<Term,Term> cur_map=homo.get(i);
                for (Map.Entry<Term, Term> set :
                        cur_map.entrySet())
                {
                    String cur_key=set.getKey().toString();
                    if (cur_key.equals(key))
                    {
                       term_lst.add(set.getValue());
                    }
                }
            }
            merged.put(key,term_lst);
        }
        System.out.println("Merged Homomorphism:"+merged);
        return merged;
    }




    /**
     * CQ minimization procedure
     *
     * Assume the body of the query from inputFile has no comparison atoms
     * but could potentially have constants in its relational atoms.
     *
     */
    public static void minimizeCQ(String inputFile, String outputFile) {
        // TODO: add your implementation
        File new_file= new File(inputFile);
        try
        {
            Query query = QueryParser.parse(Paths.get(inputFile));
            Query query_new = QueryParser.parse(Paths.get(inputFile));
            Head head = query.getHead();
            List<Variable> variables=head.getVariables();
            List<Atom> body = query.getBody();
            List<Atom> body_new=query_new.getBody();
            List<Integer> cancelled =new ArrayList<>();
            List<Integer>removed = new ArrayList<>();
            List<HashMap<Term,Term>>  homomorphism = new ArrayList<>();
            for (int  i=0;i<body.size();i++)
            {
               if (cancelled.contains(i) || removed.contains(i))
               {
                   continue;
               }
               RelationalAtom current_atom = (RelationalAtom) body.get(i);
               String cur_name = current_atom.getName();
               List <Term> cur_terms = current_atom.getTerms();
                int repeat_i=0;
               for (int j=i+1;j<body.size();j++)
               {
                   int repeat_j=0;
                   List<String> minimized=new ArrayList<>();
                   RelationalAtom temp_atom = (RelationalAtom) body.get(j);
                   String temp_name = temp_atom.getName();
                   List<Term> temp_term =temp_atom.getTerms();
                   List<Term> removed_variable=new ArrayList<>();
                   List<Term> change_to =new ArrayList<>();
                   int cnt_check=0;
                   HashMap<Term,Term> new_homo= new HashMap<Term,Term>();
                   if (cur_name.equals(temp_name))
                   {

                       if (cur_terms.size() == temp_term.size())
                       {
                           for (int k=0;k<cur_terms.size();k++)
                           {
                               if (cur_terms.get(k).toString().equals(temp_term.get(k).toString()))
                               {
                                   minimized.add(cur_terms.get(k).toString());
                                   cnt_check=cnt_check+1;
                                   continue;
                               }
                               else
                               {
                                   boolean is_cur_numeric = cur_terms.get(k) instanceof  IntegerConstant;
                                   boolean is_temp_numeric = temp_term.get(k) instanceof  IntegerConstant;
                                   boolean is_cur_StringConstants = cur_terms.get(k) instanceof StringConstant;
                                   boolean is_temp_StringConstants = temp_term.get(k) instanceof  StringConstant;
                                   boolean is_cur_variable= cur_terms.get(k) instanceof Variable;
                                   boolean is_temp_variable= temp_term.get(k) instanceof  Variable;
                                   boolean is_cur_distinguish=is_distinguished(body,variables,cur_terms.get(k).toString());
                                   boolean is_temp_distinguish=is_distinguished(body,variables,temp_term.get(k).toString());
                                   if ( is_cur_numeric && is_temp_variable && ! is_temp_distinguish)
                                   {
                                       minimized.add(cur_terms.get(k).toString());
                                       removed_variable.add(temp_term.get(k));
                                       change_to.add(cur_terms.get(k));
                                       new_homo.put(temp_term.get(k),cur_terms.get(k));
                                       cnt_check=cnt_check+1;
                                       continue;
                                   }
                                   else if (is_temp_numeric && is_cur_variable && ! is_cur_distinguish)
                                   {
                                       minimized.add(temp_term.get(k).toString());
                                       removed_variable.add(cur_terms.get(k));
                                       change_to.add(temp_term.get(k));
                                       new_homo.put(cur_terms.get(k),temp_term.get(k));
                                       cnt_check=cnt_check+1;
                                       continue;
                                   }
                                   else if (is_cur_numeric && is_temp_numeric)
                                   {
                                       if (cur_terms.get(k).toString().equals(temp_term.get(k).toString()))
                                       {
                                           minimized.add(temp_term.get(k).toString());
                                           removed_variable.add(cur_terms.get(k));
                                           change_to.add(cur_terms.get(k));
                                           new_homo.put(cur_terms.get(k),temp_term.get(k));
                                           cnt_check=cnt_check+1;
                                           continue;
                                       }
                                       else
                                       {
                                           break;
                                       }
                                   }

                                   else if (is_cur_StringConstants && is_temp_StringConstants)
                                   {
                                       if (cur_terms.get(k).toString().equals(temp_term.get(k).toString()))
                                       {
                                           minimized.add(temp_term.get(k).toString());
                                           removed_variable.add(cur_terms.get(k));
                                           change_to.add(temp_term.get(k));
                                           new_homo.put(cur_terms.get(k),temp_term.get(k));
                                           cnt_check=cnt_check+1;
                                           continue;
                                       }
                                       else

                                       {
                                           break;
                                       }
                                   }
                                   else if (is_cur_StringConstants && is_temp_variable && ! is_temp_distinguish)
                                   {

                                       minimized.add(cur_terms.get(k).toString());
                                       removed_variable.add(temp_term.get(k));
                                       change_to.add(cur_terms.get(k));
                                       new_homo.put(temp_term.get(k),cur_terms.get(k));
                                       cnt_check=cnt_check+1;
                                       continue;
                                   }
                                   else if (is_temp_StringConstants && is_cur_variable && ! is_cur_distinguish)
                                   {
                                       minimized.add(temp_term.get(k).toString());
                                       removed_variable.add(cur_terms.get(k));
                                       change_to.add(temp_term.get(k));
                                       new_homo.put(cur_terms.get(k),temp_term.get(k));
                                       cnt_check=cnt_check+1;
                                       continue;
                                   }

                                   else if(is_temp_variable && is_temp_distinguish)
                                   {
                                       if (cur_terms.get(k).toString().equals(temp_term.get(k).toString()))
                                       {
                                           minimized.add(temp_term.get(k).toString());
                                           removed_variable.add(cur_terms.get(k));
                                           change_to.add(temp_term.get(k));
                                           new_homo.put(cur_terms.get(k),temp_term.get(k));
                                           cnt_check=cnt_check+1;
                                           continue;
                                       }
                                   }
                                   else if(is_cur_variable && is_cur_distinguish)
                                   {
                                       if (temp_term.get(k).toString().equals(cur_terms.get(k).toString()))
                                       {
                                           minimized.add(temp_term.get(k).toString());
                                           removed_variable.add(cur_terms.get(k));
                                           change_to.add(temp_term.get(k));
                                           new_homo.put(cur_terms.get(k),temp_term.get(k));
                                           cnt_check=cnt_check+1;
                                           continue;
                                       }
                                   }
                                   else if ( is_temp_variable && is_cur_variable && ! (is_temp_distinguish) && !(is_cur_distinguish) )
                                   {
                                       minimized.add(temp_term.get(k).toString());
                                       removed_variable.add(cur_terms.get(k));
                                       change_to.add((temp_term.get(k)));
                                       new_homo.put(cur_terms.get(k),temp_term.get(k));
                                       cnt_check=cnt_check+1;
                                       continue;
                                   }

                               }
                           }
                       }
                       else
                       {
                         continue;
                       }
                   }
                   if (cnt_check==cur_terms.size())
                   {
                       cancelled.add(i);
                       cancelled.add(j);
                       if(if_equal(minimized,cur_terms))
                       {
                           if (! removed.contains(j))
                           {
                               removed.add(j);
                               homomorphism.add(new_homo);
                           }
                           else
                           {
                                repeat_j++;
                               for (int h=0;h<removed_variable.size();h++)
                               {
                                   new_homo.put(removed_variable.get(h),change_to.get(h));
                               }
                               homomorphism.add(new_homo);
                           }
                       }
                       else if (if_equal(minimized,temp_term))
                       {
                           if (! removed.contains(i))
                           {
                               removed.add(i);
                               homomorphism.add(new_homo);
                           }
                           else
                           {
                               repeat_i++;
                               for (int h=0;h<removed_variable.size();h++)
                               {
                                   new_homo.put(removed_variable.get(h),change_to.get(h));
                               }
                               homomorphism.add(new_homo);
                           }
                       }
                       break;
                   }
                   else
                   {
                     continue;
                   }
               }
            }
            List<Atom> temp = new ArrayList<>();
            System.out.println("Homorphorism:"+homomorphism);
            merge_homo(homomorphism);
//            removed=check_removed(body_new,body,removed_map,change_to_map,removed,Extra_remove_map,Extra_change_to_map);
//            System.out.println("removed:"+removed);
            for (int i=0;i<body_new.size();i++)
        {
            if (!removed.contains(i))
            {
                temp.add(body_new.get(i));
            }
        }
            String output= Minimized(head,temp);
            System.out.println(output);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile,false));
            writer.write(output);
            writer.close();


        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Example method for getting started with the parser.
     * Reads CQ from a file and prints it to screen, then extracts Head and Body
     * from the query and prints them to screen.
     */

    public static void parsingExample(String filename) {

        try {
            Query query = QueryParser.parse(Paths.get(filename));
            // Query query = QueryParser.parse("Q(x, y) :- R(x, z), S(y, z, w)");
            // Query query = QueryParser.parse("Q(x) :- R(x, 'z'), S(4, z, w)");

            System.out.println("Entire query: " + query);
            Head head = query.getHead();
            System.out.println("Head: " + head);
            List<Atom> body = query.getBody();
            System.out.println("Body: " + body);
        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

}
