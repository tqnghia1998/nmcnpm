using System.Security;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Interaction logic for CreateSubjectPage.xaml
    /// </summary>
    public partial class ListSubjectPage : BasePage<ListSubjectViewModel>
    {
        public ListSubjectPage() : base()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Constructor with view model
        /// </summary>
        /// <param name="specificViewModel">The specific view model to use for this page</param>
        public ListSubjectPage(ListSubjectViewModel specificViewModel = null) : base(specificViewModel)
        {
            InitializeComponent();
        }
    }
}
