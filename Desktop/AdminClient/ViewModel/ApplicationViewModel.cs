using DbModel;

namespace AdminClient
{
    /// <summary>
    /// The application state as a view model
    /// </summary>
    public class ApplicationViewModel : BaseViewModel
    {
        /// <summary>
        /// The current page of the application
        /// </summary>
        public ApplicationPage CurrentPage { get; private set; } = ApplicationPage.Login;

        /// <summary>
        /// The view model to use for the current page when the CurrentPage changes
        /// NOTE: This is not a live up-to-date view model of the current page
        ///       it is simply used to set the view model of the current page
        ///       at the time it changes
        /// </summary>
        public BaseViewModel CurrentPageViewModel { get; set; }

        /// <summary>
        /// True if the side menu should be shown
        /// </summary>
        public bool SideMenuVisible { get; set; } = false;

        /// <summary>
        /// Navigates to the specified page
        /// </summary>
        /// <param name="page">The page to go to</param>
        /// <param name="viewModel">The view model, if any, to set explicitly to the new page</param>
        public void GoToPage(ApplicationPage page, BaseViewModel viewModel = null)
        {
            //Set the view model
            CurrentPageViewModel = viewModel;

            // Set the current page
            CurrentPage = page;

            // Fire off a CurrentPage change event
            OnPropertyChanged(nameof(CurrentPage));

            // Show side menu or not
            SideMenuVisible = (page != ApplicationPage.Login && page != ApplicationPage.Register);
        }

        /// <summary>
        /// Handle what happen when we have successful logged in
        /// </summary>
        /// <param name="userData">The result from the successful login</param>
        public void HandleSuccessfulLogin(LoginResultApiModel userData)
        {
            // Store this in the client data store
            IoC.ClientDataStore.ApplicationUser = new LoginCredentialsDataModel
            {
                Username = userData.Username,
                FirstName = userData.FirstName,
                LastName = userData.LastName,
                Email = userData.Email,
                Token = userData.Token

            };

            // Load new information
            IoC.SideMenu.LoadInformation();

            // Go to chat page
            IoC.Application.GoToPage(ApplicationPage.CreateSubject);
        }
    }
}
