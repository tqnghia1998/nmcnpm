using DbModel;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace AdminClient
{
    /// <summary>
    /// The View Model for a login screen
    /// </summary>
    public class LoginViewModel : BaseViewModel
    {

        #region Public Properties

        /// <summary>
        /// The email of user
        /// </summary>
        public string Email { set; get; }

        public bool LoginIsRunning { get; set; }

        #endregion

        #region Commands

        /// <summary>
        /// The command to login
        /// </summary>
        public ICommand LoginCommand { set; get; }

        /// <summary>
        /// The command to register
        /// </summary>
        public ICommand RegisterCommand { set; get; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public LoginViewModel()
        {
            // Create commands
            LoginCommand = new RelayParameterizedCommand(LoginAsync);
            RegisterCommand = new RelayCommand(async () => await RegisterAsync());
        }

        #endregion

        /// <summary>
        /// Attempts to log the user in
        /// </summary>
        /// <param name="parameter">The <see cref="SecureString"/> passed in from the view for users password</param>
        /// <returns></returns>
        private async void LoginAsync(object parameter)
        {
            await RunCommand(() => this.LoginIsRunning, async () =>
            {
                // Call the server and attempt to login with credentials
                // TODO: Move all URLs and API routes to static class in core
                var result = await WebRequests.PostAsync<ApiResponse<LoginResultApiModel>>("http://localhost:51197/api/account/login",
                    new LoginCredentialsApiModel()
                    {
                        UsernameOrEmail = Email,
                        Password = (parameter as IHavePassword).SecurePassword.Unsecure()
                    });

                // If the response has an error...
                if (result.DisplayErrorIfFailed("Login failed"))
                {
                    // We are done
                    return;
                }

                //OK successful logged in... now get the user data
                var loginResult = result.ServerResponse.Response;

                // Let application view model handle what happens
                // with the successful login
                IoC.Application.HandleSuccessfulLogin(loginResult);
            });
        }

        /// <summary>
        /// Take the user to the register page
        /// </summary>
        /// <param name="parameter">The <see cref="SecureString"/> passed in from the view for users password</param>
        /// <returns></returns>
        private async Task RegisterAsync()
        {
            // TODO: go to the register page
            IoC.Application.GoToPage(ApplicationPage.Register);
            await Task.Delay(1);
        }
    }
}
