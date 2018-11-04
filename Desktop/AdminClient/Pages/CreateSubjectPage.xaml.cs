namespace AdminClient
{
    /// <summary>
    /// Interaction logic for CreateSubjectPage.xaml
    /// </summary>
    public partial class CreateSubjectPage : BasePage<CreateSubjectPageViewModel>
    {
        public CreateSubjectPage(): base()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Constructor with view model
        /// </summary>
        /// <param name="specificViewModel">The specific view model to use for this page</param>
        public CreateSubjectPage(CreateSubjectPageViewModel specificViewModel = null) : base(specificViewModel)
        {
            InitializeComponent();
        }
    }
}
