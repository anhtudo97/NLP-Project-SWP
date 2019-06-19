using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace FilterUnicode
{
    class Sentence
    {
        //2 dau gach ngang
        const string pattern1 = @"^\s*\d{1,2}-\d{1,2}-\d{2,4}[\.,\?]? ";
        const string pattern2 = @" \d{1,2}-\d{1,2}-\d{2,4}[\.|\?]$";
        const string pattern3 = @" \d{1,2}-\d{1,2}-\d{2,4}[\.,\?]? ";
        //1 dau gach ngang
        const string pattern4 = @"^\s*\d{1,2}-\d{1,2}[\.,\?]? ";
        const string pattern5 = @" \d{1,2}-\d{1,2}[\.,\?]? ";
        const string pattern6 = @" \d{1,2}-\d{1,2}[\.|\?]$";

        const string pattern_4 = @"^\s*\d{1,2}-\d{4}[\.,\?]? ";
        const string pattern_5 = @" \d{1,2}-\d{4}[\.,\?]? ";
        const string pattern_6 = @" \d{1,2}-\d{4}[\.|\?]$";
        //2 dau gach cheo
        const string pattern7 = @"^\s*\d{1,2}/\d{1,2}/\d{2,4}[\.,\?]? ";
        const string pattern8 = @" \d{1,2}/\d{1,2}/\d{2,4}[\.|\?]$";
        const string pattern9 = @" \d{1,2}/\d{1,2}/\d{2,4}[\.,\?]? ";
        //1 dau gach cheo
        const string pattern10 = @"^\s*\d{1,2}/\d{1,2}[\.,\?]? ";
        const string pattern11 = @" \d{1,2}/\d{1,2}[\.,\?]? ";
        const string pattern12 = @" \d{1,2}/\d{1,2}[\.|\?]$";

        const string pattern_10 = @"^\s*\d{1,2}/\d{4}[\.,\?]? ";
        const string pattern_11 = @" \d{1,2}/\d{4}[\.,\?]? ";
        const string pattern_12 = @" \d{1,2}/\d{4}[\.|\?]$";
        //1 dau hai cham
        const string pattern13 = @"^\s*\d{1,2}:\d{1,2}[\.,\?]? ";
        const string pattern14 = @" \d{1,2}:\d{1,2}[\.,\?]? ";
        const string pattern15 = @" \d{1,2}:\d{1,2}[\.|\?]?$";
        string Content { get; set; }
        int DashNumber { get; set; }
        int SlashNumber { get; set; }

        int ColonNumber { get; set; }
        public Sentence(string Content)
        {
            this.Content = Content;
            getNumberOfChar();

        }
        public void getNumberOfChar()
        {
            this.DashNumber = getNumberAppear(Content, '-');
            this.SlashNumber = getNumberAppear(Content, '/');
            this.ColonNumber = getNumberAppear(Content, ':');
        }
        public string checkDateTime()
        {
            if (DashNumber > 0)
            {
                if (!checkDash())
                {
                    return "";
                }
            }
            if (SlashNumber > 0)
            {
                if (!checkSlash())
                {
                    return "";
                }
            }
            if (ColonNumber > 0)
            {
                if (!checkSlash())
                {
                    return "";
                }
            }
            return Content;
        }

        private int getNumberAppear(string content, char v)
        {
            int count = 0;
            if (Content.Contains(v))
            {
                foreach (char item in Content)
                {
                    if (item == v)
                    {
                        count++;
                    }
                }
            }
            return count;
        }
        public bool checkDash()
        {
            int count = 0;
            MatchCollection matches;
            matches = Regex.Matches(Content, pattern1);
            if (matches.Count>0)
            {
                count += matches.Count * 2;
            }
            matches = Regex.Matches(Content, pattern2);
            if (matches.Count > 0)
            {
                count += matches.Count * 2;
            }
            matches = Regex.Matches(Content, pattern3);
            if (matches.Count > 0)
            {
                count += matches.Count * 2;
            }

            matches = Regex.Matches(Content, pattern4);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern5);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern6);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern_4);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern_5);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern_6);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            if (count == DashNumber)
            {
                return true;
            }

            return false ;
        }
        public bool checkSlash()
        {
            int count = 0;
            MatchCollection matches;
            matches = Regex.Matches(Content, pattern7);
            if (matches.Count > 0)
            {
                count += matches.Count * 2;
            }
            matches = Regex.Matches(Content, pattern8);
            if (matches.Count > 0)
            {
                count += matches.Count * 2;
            }
            matches = Regex.Matches(Content, pattern9);
            if (matches.Count > 0)
            {
                count += matches.Count * 2;
            }

            matches = Regex.Matches(Content, pattern10);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern11);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern12);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern_10);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern_11);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern_12);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            if (count == SlashNumber)
            {
                return true;
            }

            return false;
        }
        public bool checkColon()
        {
            int count = 0;
            MatchCollection matches;
            matches = Regex.Matches(Content, pattern13);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern14);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            matches = Regex.Matches(Content, pattern15);
            if (matches.Count > 0)
            {
                count += matches.Count;
            }
            if (count == ColonNumber)
            {
                return true;
            }

            return false;
        }
    }
}
