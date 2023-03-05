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
    public  static  boolean is_distinguished(String check_value, Head head)
    {
         List<Variable> temp = head.getVariables();
         for(int i=0;i<temp.size();i++)
         {
             if (temp.get(i).toString().equals(check_value))
             {
                 return true;
             }
         }
         return false;
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
    public static  List<Integer> minimization(HashMap<String,List<Term>> homomorphism, List<Atom>body, List<Atom>body_copy,Head head )
    {
        System.out.println("Merged homo:"+homomorphism);
        List<String> allowed_key = new ArrayList<>();
        for (Map.Entry<String, List<Term>> map : homomorphism.entrySet())
        {
            String remove_term=map.getKey();
            List<Term> term_lst=map.getValue();
            List<Term> allowed=new ArrayList<>();
           for(int j=0;j<term_lst.size();j++)
           {
            Term set_value=term_lst.get(j);
            for(int i=0;i<body.size();i++)
            {
                RelationalAtom temp_atom=(RelationalAtom)body.get(i);
                if(Is_contain_term(remove_term,temp_atom).get(0).equals("true"))
                {
                    int index = Integer.parseInt(Is_contain_term(remove_term,temp_atom).get(1));
                    // replace the value with set value
                    if (!is_distinguished(remove_term,head))
                    {
                        Term temp = temp_atom.getTerms().get(index);
                        temp_atom.getTerms().set(index,set_value);
                        List<Atom> newbody=new ArrayList<>();
                        newbody = Set_body(body,remove_term,set_value);
                        boolean is_subset= Is_subset(temp_atom,body_copy,head);
                        boolean is_subset_all = Is_subset(newbody,body_copy,head);
                        if(is_subset && is_subset_all)
                        {
                           allowed.add(set_value);
                           allowed_key.add(remove_term);
                        }
                        else
                        {
                           temp_atom.getTerms().set(index,temp);
                           continue;
                        }
                    }
                    else
                    {
                        continue;
                    }
                }
            }
           }
           homomorphism.put(remove_term,allowed);
        }
        HashMap<String,List<Term>> after_removed_homo=new HashMap<String,List<Term>>();
        for(Map.Entry<String,List<Term>> map: homomorphism.entrySet())
        {
            String key= map.getKey();
            if (allowed_key.contains(key))
            {
                after_removed_homo.put(key,homomorphism.get(key));
            }
        }
        List<Integer> removed = new ArrayList<>();
        for(Map.Entry<String,List<Term>> map: after_removed_homo.entrySet())
        {
              String remove_term=map.getKey();
              for(int i=0;i<body_copy.size();i++)
              {
                  RelationalAtom atom=(RelationalAtom) body_copy.get(i);
                  List<String> contain=Is_contain_term(remove_term,atom);
                  if (contain.get(0).toString().equals("true") && map.getValue().size()>0)
                  {
                      removed.add(i);
                  }

              }
        }
        System.out.println("Removed:"+removed);
        return removed;
    }
    public static boolean Is_subset( RelationalAtom target_atom ,List<Atom>body_copy,Head head)
    {
       boolean is_same=Is_same(target_atom,body_copy,head);
       boolean is_similar=Is_similar(target_atom,body_copy,head);
       return is_same || is_similar;
    }

    public static boolean Is_subset( List<Atom> body,List<Atom>body_copy,Head head)
    {
        for(int i=0;i<body.size();i++)
        {
            RelationalAtom body_atom =(RelationalAtom) body.get(i);
            if (! Is_subset(body_atom,body_copy,head))
            {
                return false;
            }
        }
        return true;
    }
    public static List<Atom> Set_body(List<Atom> body, String target, Term set_value)
    {
        for (int i =0; i<body.size();i++)
        {
            RelationalAtom cur_atom = (RelationalAtom) body.get(i);
            List<String> check_contain_term = Is_contain_term(target,cur_atom);
            if(check_contain_term.get(0).equals("true"))
            {
                int index=Integer.parseInt(check_contain_term.get(1));
                ((RelationalAtom) body.get(i)).getTerms().set(index,set_value);
            }
        }
        List<Atom> new_body= new ArrayList<>();
        new_body=body;
        return new_body;
    }

    public static  boolean Is_same( RelationalAtom target_atom ,List<Atom>body_copy, Head head)
    {

        for (int i=0;i<body_copy.size();i++)
        {
            RelationalAtom body_relation_atom=(RelationalAtom) body_copy.get(i);
            if (body_relation_atom.equals(target_atom))
            {
                return true;
            }
        }
        return false;
    }
    public  static  boolean Is_similar(RelationalAtom target_atom,List<Atom> body_copy,Head head)
    {
        RelationalAtom atom = (RelationalAtom) body_copy.get(0);
        for(int i=0;i<body_copy.size();i++)
        {
            RelationalAtom body_relation_atom =(RelationalAtom) body_copy.get(i);
            if (body_relation_atom.Is_similar(target_atom,head))
            {
                return true;
            }
        }
        return false;
    }
    public static List<String> Is_contain_term(String term , RelationalAtom relationalAtom)
    {
        for(int i=0;i<relationalAtom.getTerms().size();i++)
        {
            if (relationalAtom.getTerms().get(i).toString().equals(term))
            {
                List<String> check=new ArrayList<>();
                check.add("true");
                check.add(Integer.toString(i));
                return check;
            }
        }

        List<String> check=new ArrayList<>();
        check.add("false");
        check.add(Integer.toString(Integer.MAX_VALUE));
        return check;
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
            term_lst=remove_duplicate(term_lst);
            merged.put(key,term_lst);
        }
        return merged;
    }



    public static List<Term> remove_duplicate(List<Term>lst)
    {
         List<String> temp =new ArrayList<>();
         List<Integer> removed_index=new ArrayList<>();
         for(int i=0;i<lst.size();i++)
         {
             if (!temp.contains(lst.get(i).toString()))
             {
                 temp.add(lst.get(i).toString());
             }
             else
             {
                 removed_index.add(i);
             }
         }
         List<Term> new_lst=new ArrayList<>();
         for(int i =0;i<lst.size();i++)
         {
             if (!removed_index.contains(i))
             {
                 Term new_term= lst.get(i);
                 new_lst.add(new_term);
             }
         }
         return new_lst;
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
            List<Atom> body = query.getBody();
            List<Atom> body_new=query_new.getBody();
            List<HashMap<Term,Term>>  homomorphism = new ArrayList<>();
            for (int  i=0;i<body.size();i++)
            {
               RelationalAtom current_atom = (RelationalAtom) body.get(i);
               String cur_name = current_atom.getName();
               List <Term> cur_terms = current_atom.getTerms();
                int repeat_i=0;
               for (int j=i+1;j<body.size();j++)
               {
                   int repeat_j=0;
                   RelationalAtom temp_atom = (RelationalAtom) body.get(j);
                   String temp_name = temp_atom.getName();
                   List<Term> temp_term =temp_atom.getTerms();
                   int cnt_check=0;
                   HashMap<Term,Term> new_homo= new HashMap<Term,Term>();
                   if (cur_name.equals(temp_name))
                   {
                       if (cur_terms.size() == temp_term.size())
                       {
                           for (int k=0;k<cur_terms.size();k++)
                           {
                               Term cur=cur_terms.get(k);
                               Term temp = temp_term.get(k);
                               boolean is_cur_constant = cur_terms.get(k) instanceof  Constant;
                               boolean is_temp_constant = temp_term.get(k) instanceof  Constant;
                               boolean is_cur_variable= cur_terms.get(k) instanceof Variable;
                               boolean is_temp_variable= temp_term.get(k) instanceof  Variable;
                               boolean is_cur_distinguish=is_distinguished(cur_terms.get(k).toString(),head);
                               boolean is_temp_distinguish=is_distinguished(temp_term.get(k).toString(),head);
                               if (is_cur_constant && is_temp_constant)
                               {
                                   if(cur.toString().equals(temp.toString()))
                                   {
                                       cnt_check=cnt_check+1;
                                   }

                               }
                               else if (is_cur_constant && is_temp_variable && !is_temp_distinguish)
                               {
                                   cnt_check++;
                                   new_homo.put(temp,cur);
                               }
                               else if (is_cur_variable && is_temp_constant && !is_cur_distinguish)
                               {
                                   cnt_check++;
                                   new_homo.put(cur,temp);
                               }
                               else if (is_cur_variable && is_temp_variable)
                               {
                                    if(!is_cur_distinguish && !is_temp_distinguish)
                                    {
                                        if(! cur.toString().equals(temp.toString()))
                                        {
                                            new_homo.put(cur,temp);
                                            new_homo.put(temp,cur);
                                            cnt_check++;
                                        }
                                        else
                                        {
                                           cnt_check++;
                                        }
                                    }
                                    else if ( is_cur_distinguish && ! is_temp_distinguish)
                                    {
                                         new_homo.put(temp,cur);
                                         cnt_check++;

                                    }
                                    else if (is_temp_distinguish && ! is_cur_distinguish)
                                    {
                                         new_homo.put(cur,temp);
                                         cnt_check++;
                                    }
                               }
                           }
                           homomorphism.add(new_homo);
                       }
                       else
                       {
                         continue;
                       }
                   }
                   else
                   {
                     continue;
                   }
               }
            }
            List<Atom> temp = new ArrayList<>();
            HashMap<String,List<Term>> merged_homo= merge_homo(homomorphism);
            List<Integer> removed =new ArrayList<>();
            removed=minimization(merged_homo,body,body_new,head);
            System.out.println("New_body:"+body_new);
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
