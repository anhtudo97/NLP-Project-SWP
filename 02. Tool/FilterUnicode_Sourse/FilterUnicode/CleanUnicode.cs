using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Web.Script.Serialization;
using System.Windows.Forms;

namespace FilterUnicode
{
    class CleanUnicode
    {
        public UnicodeModel um { get; set; }
        public const string jsonFile = "Unicode.json";
        public string Content { get; set; }
        public string FilePath;
        public List<int> listSomeThing;
        public CleanUnicode()
        {
            listSomeThing = new List<int>();
        }
        public bool loadJson()
        {
            try
            {
                um = DeserializeNames();
                if (um == null)
                {
                    um = new UnicodeModel();
                }
            }
            catch (Exception)
            {
                MessageBox.Show(jsonFile + " is fail", "Error", MessageBoxButtons.OK, MessageBoxIcon.Stop);
                return false;
            }
            return true;
        }
        public UnicodeModel DeserializeNames()
        {
            string json = File.ReadAllText(jsonFile);
            JavaScriptSerializer ser = new JavaScriptSerializer();
            return ser.Deserialize<UnicodeModel>(json);
        }
        public List<int> CleanUni(BackgroundWorker bw)
        {
            listSomeThing = new List<int>();
            string pattern = @"(^\s*.*(\.|\?)\s*\n)";
            string replaceWith = "$1";
            MatchCollection matches;
            matches = Regex.Matches(Content, pattern, RegexOptions.Multiline);
            string[] content = new string[matches.Count];
            listSomeThing.Add(matches.Count);
            bool checkItem = true;
            int index = 0;
            List<string> listContent = new List<string>();
            for (int i = 0; i < matches.Count; i++)
            {
                checkItem = true;
                string match = matches[i].Value.ToString();
                if (Regex.IsMatch(match, @".*\w\.\w.*"))
                {
                    continue;
                }
                for (int j = 0; j < match.Length; j++)
                {

                    
                    if (um.Unicode.Contains(match[j]) || match[j] == '\r' || match[j] == '\n' || match[j] == ' ')
                    {
                        continue;
                    }
                    else
                    {
                        match = match.Replace(match[j] + "", " ");
                    }
                }
                if (Regex.Split(match, "\\s+").Length >= 10)
                {
                    match = match.Replace("  ", "");
                    listContent.Add(match.Trim());
                    int result = (i + 1) * 100 / matches.Count;
                    bw.ReportProgress(result);
                }
            }
            content = listContent.ToArray();
            content = content.Distinct().ToArray();
            content = FilterDate(content);
            content = removeEmptyLine(content);
            listSomeThing.Add(content.Length);
            listSomeThing.Add(matches.Count - content.Length);
            File.WriteAllLines(FilePath, content, Encoding.UTF8);
            return listSomeThing;
        }

        private string[] FilterDate(string[] content)
        {
            for (int i = 0; i < content.Length; i++)
            {
                Sentence sentence = new Sentence(content[i]);
                content[i] = sentence.checkDateTime();
            }
            return content;
        }

        public bool checkLenght(string line)
        {
            string[] splitLine = Regex.Split(line, "\\s+");
            if (splitLine.Length < 10)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        public string ReplaceNull(string contentLine)
        {
            if (contentLine == null)
            {
                contentLine = "";
            }
            return contentLine;
        }
        public string ReadFileContent(string FilePath)
        {
            string _content = File.ReadAllText(FilePath, Encoding.UTF8);
            this.Content = _content;
            this.FilePath = FilePath;
            return _content;
        }
        public string[] removeEmptyLine(string[] content)
        {
            int Count = 0;
            for (int i = content.Length - 1; i >= 0; i--)
            {
                if (content[i].Trim() == "")
                {
                    Count++;
                }
            }
            string[] newContent = new string[content.Length - Count];
            int index = 0;
            foreach (string item in content)
            {
                if (item.Trim() != "")
                {
                    newContent[index] = item;
                    index++;
                }
            }

            return newContent;
        }
    }
}
