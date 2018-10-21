using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace AdminClient
{
    /// <summary>
    /// Interaction logic for CreateSubjectPage.xaml
    /// </summary>
    public partial class CreateSubjectPage : UserControl
    {
        public CreateSubjectPage()
        {
            InitializeComponent();

            DataContext = new CreateSubjectPageViewModel
            {
                Major = new TextEntryViewModel { Label = "Major" },
                ID = new TextEntryViewModel { Label = "ID" },
                Subject = new TextEntryViewModel { Label = "Subject" },
                Teacher = new TextEntryViewModel { Label = "Teacher" },
                Class = new TextEntryViewModel { Label = "Class" },
            };
           
        }
    }
}
