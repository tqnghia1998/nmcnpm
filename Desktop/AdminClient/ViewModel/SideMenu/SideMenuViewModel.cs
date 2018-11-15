using System.Windows;
using System.Windows.Input;

namespace AdminClient
{
    /// <summary>
    /// A view model for <see cref="SideMenuControl"/>
    /// </summary>
    public class SideMenuViewModel: BaseViewModel
    {
        #region Public Properties

        /// <summary>
        /// Background header
        /// </summary>
        public string Background { get; set; } = "/Images/header-background.png";

        /// <summary>
        /// Avatar of admin
        /// </summary>
        public string ProfilePicture { get; set; } = "/Images/avatar.jpg";

        /// <summary>
        /// Name
        /// </summary>
        public string Name { get; set; } = "Nguyễn Ngọc Nghĩa";

        public SideMenuItemViewModel CreateSubjectItem { get; set; } = new SideMenuItemViewModel();

        #endregion

        #region Public Commands

        /// <summary>
        /// Go to create subject page
        /// </summary>
        public ICommand OpenCreateSubjectPageCommand { get; set; }


        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public SideMenuViewModel()
        {
            OpenCreateSubjectPageCommand = new RelayCommand(OpenCreateSubjectPage);
        }

        #endregion

        #region Command Methods

        public void OpenCreateSubjectPage()
        {
            IoC.Application.GoToPage(ApplicationPage.CreateSubject);
        }

        /// <summary>
        /// Sets the side menu view model properties based on the data in the client data store
        /// </summary>
        public void LoadInformation()
        {
            var storeData = IoC.ClientDataStore.ApplicationUser;
            Name = $"{storeData.FirstName} {storeData.LastName}";
        }

        #endregion
    }
}
