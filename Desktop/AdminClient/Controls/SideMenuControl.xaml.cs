using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Interaction logic for SideMenuControl.xaml
    /// </summary>
    public partial class SideMenuControl : UserControl
    {
        public SideMenuControl()
        {
            InitializeComponent();
            DataContext = new SideMenuViewModel();
        }
    }
}
