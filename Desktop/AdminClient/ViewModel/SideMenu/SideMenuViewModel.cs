using DbModel;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
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

        public ObservableCollection<SideMenuItemViewModel> Items { get; set; } = new ObservableCollection<SideMenuItemViewModel>();

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public SideMenuViewModel()
        {
            Items.Add(new SideMenuItemViewModel(async () => await OpenUpdateSubjectPage())
            {
                Icon = "\uf12d",
                Content = "List subject"
            });

            Items.Add(new SideMenuItemViewModel(OpenCreateSubjectPage)
            {
                Icon = "\uf044",
                Content = "Create subject"
            });

            
        }

        #endregion

        #region Command Methods

        /// <summary>
        /// Go to create subject page
        /// </summary>
        public void OpenCreateSubjectPage()
        {
            IoC.Application.GoToPage(ApplicationPage.CreateSubject);
        }

        /// <summary>
        /// Go to update subject page
        /// </summary>
        public async Task OpenUpdateSubjectPage()
        {
            //var result = await WebRequests.GetAsync<List<string>>("http://localhost:51197/api/subject");
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
