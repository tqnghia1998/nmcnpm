using System.ComponentModel;
using System.Windows.Controls;

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

            DataContext = new CreateSubjectPageViewModel()
            {
                Major = new TextEntryViewModel { Label = "Major" },
                ID = new TextEntryViewModel { Label = "ID" },
                Subject = new TextEntryViewModel { Label = "Subject" },
                Credit = new TextEntryViewModel { Label = "Credit"},
                Teacher = new TextEntryViewModel { Label = "Teacher" },
                Class = new TextEntryViewModel { Label = "Class" },
            };

        }
    }
}
