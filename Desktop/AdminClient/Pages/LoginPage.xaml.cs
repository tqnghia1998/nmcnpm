using System.Security;
using System.Windows.Controls;

namespace AdminClient
{
    /// <summary>
    /// Interaction logic for LoginPage.xaml
    /// </summary>
    public partial class LoginPage : BasePage<LoginViewModel>, IHavePassword
    {
        public LoginPage(): base()
        {
            InitializeComponent();
        }

        public LoginPage(LoginViewModel specificViewModel = null) : base(specificViewModel)
        {
            InitializeComponent();
        }

        /// <summary>
        /// The secure password for this login page
        /// </summary>
        public SecureString SecurePassword => PasswordText.SecurePassword;
    }
}
