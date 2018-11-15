using System.Diagnostics;

namespace AdminClient
{
    /// <summary>
    /// Convert the <see cref="ApplicationPage"/> to an actual view/page
    /// </summary>
    public static class ApplicationPageHelpers
    {
        /// <summary>
        /// Take a <see cref="ApplicationPage"/> and a view model, if any, and creates the desired page
        /// </summary>
        /// <param name="page"></param>
        /// <param name="viewModel"></param>
        /// <returns></returns>
        public static BasePage ToBasePage(this ApplicationPage page, object viewModel = null)
        {
            // Find the appropriate page
            switch (page)
            {
                case ApplicationPage.Login:
                    return new LoginPage(viewModel as LoginViewModel);
                case ApplicationPage.Register:
                    return new RegisterPage(viewModel as RegisterViewModel);
                case ApplicationPage.CreateSubject:
                    return new CreateSubjectPage(viewModel as CreateSubjectPageViewModel);
                default:
                    Debugger.Break();
                    return null;
            }
        }

        /// <summary>
        /// Convert a <see cref="BasePage"/> to the specific <see cref="ApplicationPage"/> that is for that type of page
        /// </summary>
        /// <param name="basePage"></param>
        /// <returns></returns>
        public static ApplicationPage ToApplicationPage(this BasePage page)
        {
            // Find application page that matches the base page
            if (page is LoginPage)
            {
                return ApplicationPage.Login;
            }
            else if (page is RegisterPage)
            {
                return ApplicationPage.Register;
            }
            else if (page is CreateSubjectPage)
            {
                return ApplicationPage.CreateSubject;
            }
            else
            {
                // Alert developer of issue
                Debugger.Break();
                return default(ApplicationPage);
            }
        }
    }
}
