using System.Security;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Interaction logic for LoginPage.xaml
    /// </summary>
    public partial class RegisterPage : BasePage<RegisterViewModel>, IHavePassword, IHaveRetypePassword
    {
        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public RegisterPage() : base()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Constructor with view model
        /// </summary>
        /// <param name="specificViewModel">The specific view model to use for this page</param>
        public RegisterPage(RegisterViewModel specificViewModel) : base(specificViewModel)
        {
            InitializeComponent();
        }

        #endregion

        /// <summary>
        /// The secure password for this login page
        /// </summary>
        public SecureString SecurePassword => PasswordText.SecurePassword;

        /// <summary>
        /// Check retype password match password
        /// </summary>
        public bool RetypePasswordIsCorrect => PasswordText.SecurePassword.Unsecure().Equals(Retype.SecurePassword.Unsecure());
    }
}
