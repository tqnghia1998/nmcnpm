using DbModel;
using System;
using System.Runtime.InteropServices;
using System.Security;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace AdminClient
{
    /// <summary>
    /// The View Model for a register screen
    /// </summary>
    public class RegisterViewModel : BaseViewModel
    {

        #region Public Properties

        /// <summary>
        /// The username of user
        /// </summary>
        public string Username { set; get; }

        /// <summary>
        /// The first name  of user
        /// </summary>
        public string FirstName { set; get; }

        /// <summary>
        /// The last name of user
        /// </summary>
        public string LastName { set; get; }

        /// <summary>
        /// The email of user
        /// </summary>
        public string Email { set; get; }

        /// <summary>
        /// A flag indicating if the register command is running
        /// </summary>
        public bool RegisterIsRunning { get; set; }

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
        public RegisterViewModel()
        {
            // Create commands
            RegisterCommand = new RelayParameterizedCommand(async (parameter) => await RegisterAsync(parameter));
            LoginCommand = new RelayCommand(async () => await LoginAsync());

        }

        #endregion

        /// <summary>
        /// Attempts to register a new user
        /// </summary>
        /// <param name="parameter">The <see cref="SecureString"/> passed in from the view for users password</param>
        /// <returns></returns>
        private async Task RegisterAsync(object parameter)
        {
            await RunCommand(() => this.RegisterIsRunning, async () =>
            {

                // Check retype password
                if(!(parameter as IHaveRetypePassword).RetypePasswordIsCorrect)
                {
                    // Show error
                    MessageBox.Show("Retype password is incorrect", "Register failed", MessageBoxButton.OK, MessageBoxImage.Error);

                    // Done
                    return;
                }

                // Call the server and attempt to register with credentials
                // TODO: Move all URLs and API routes to static class in core
                var result = await WebRequests.PostAsync<ApiResponse<RegisterResultApiModel>>("http://localhost:51197/api/account/register",
                    new RegisterCredentialsApiModel()
                    {
                        Username = Username,
                        FirstName = FirstName,
                        LastName = LastName,
                        Email = Email,
                        Password = (parameter as IHavePassword).SecurePassword.Unsecure()
                    });

                // If the response has an error...
                if (result.DisplayErrorIfFailed("Register failed"))
                {
                    // We are done
                    return;
                }

                //OK successful register (and logged in)... now get the user data
                var loginResult = result.ServerResponse.Response;

                // Let application view model handle what happens
                // with the successful login
                IoC.Application.HandleSuccessfulLogin(loginResult);
            });
        }

        /// <summary>
        /// Take the user to the login page
        /// </summary>
        /// <param name="parameter">The <see cref="SecureString"/> passed in from the view for users password</param>
        /// <returns></returns>
        private async Task LoginAsync()
        {
            // TODO: go to the register page
            IoC.Application.GoToPage(ApplicationPage.Login);
            await Task.Delay(1);
        }
    }
}
