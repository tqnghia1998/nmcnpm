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

        /// <summary>
        /// 
        /// </summary>
        public SideMenuItemViewModel CreateSubjectItem { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public SideMenuItemViewModel UpdateSubjectItem { get; set; }

        #endregion

        #region Public Commands

        /// <summary>
        /// Go to create subject page
        /// </summary>
        public ICommand OpenCreateSubjectPageCommand { get; set; }

        /// <summary>
        /// Go to update subject page
        /// </summary>
        public ICommand OpenUpdateSubjectPageCommand { get; set; }


        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public SideMenuViewModel()
        {
            CreateSubjectItem = new SideMenuItemViewModel
            {
                Icon = "\uf044",
                Content = "Create subject"
            };

            UpdateSubjectItem = new SideMenuItemViewModel
            {
                Icon = "\uf044",
                Content = "Update subject"
            };

            OpenCreateSubjectPageCommand = new RelayCommand(OpenCreateSubjectPage);
            OpenUpdateSubjectPageCommand = new RelayCommand(OpenUpdateSubjectPage);
        }

        #endregion

        #region Command Methods

        public void OpenCreateSubjectPage()
        {
            IoC.Application.GoToPage(ApplicationPage.CreateSubject);
        }

        public void OpenUpdateSubjectPage()
        {
            IoC.Application.GoToPage(ApplicationPage.ListSubject);
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
