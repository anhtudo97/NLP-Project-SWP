using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace FilterUnicode
{
    public partial class Form1 : Form
    {
        private string fileExport;
        private CleanUnicode cUnicode;

        public Form1()
        {
            InitializeComponent();
            CheckForIllegalCrossThreadCalls = true;
        }

        private void btnBrowse_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "txt files (*.txt)|*.txt|All files (*.*)|*.*";
            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                fileExport = openFileDialog.FileName;
                txtPath.Text = fileExport;
            }
        }

        private void btnClean_Click(object sender, EventArgs e)
        {
            path = txtPath.Text.Trim();
            lblDone.Visible = false;
            bw.WorkerReportsProgress = true;
            bw.RunWorkerAsync();
        }

        private string path;
        private List<int> listSomeThing;
        private void bw_DoWork(object sender, DoWorkEventArgs e)
        {
            
            BackgroundWorker bwk = sender as BackgroundWorker;
            cUnicode = new CleanUnicode();
            if (!cUnicode.loadJson())
            {
                return;
            }
            cUnicode.ReadFileContent(path);
            listSomeThing = cUnicode.CleanUni(bwk);
        }

        private void bw_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            progressBar1.Value = e.ProgressPercentage;
        }

        private void bw_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            
            lblTotal.Text = "Total: " + listSomeThing[0];
            lblResult.Text = "Filter: " + listSomeThing[1];
            lblError.Text = "Error: " + listSomeThing[2];
            Thread.Sleep(500);
            progressBar1.Value = progressBar1.Maximum;
            lblDone.Visible = true;
            lblDone.Text = "Done: " + listSomeThing[1];
        }
    }
}
