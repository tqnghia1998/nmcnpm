using DbModel;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
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
            Items.Add(new SideMenuItemViewModel(OpenListSubjectPage)
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
        public async void OpenListSubjectPage()
        {
            var result = await WebRequests.GetAsync<ApiResponse<ListSubjectResultApiModel>>("http://localhost:51197/api/subject/listsubject");

            // If the response has an error...
            if (result.DisplayErrorIfFailed("Get list subject failed"))
            {
                // We are done
                IoC.Application.GoToPage(ApplicationPage.ListSubject);
                return;
            }

            ListSubjectViewModel viewmodel = new ListSubjectViewModel();
            var listSubject = result.ServerResponse.Response.ListSubject;

            var temp = listSubject.GroupBy(item => item.Major).Select(item => item.Key).ToList();
            viewmodel.ListMajor = new ObservableCollection<string>(temp);

            temp = listSubject.GroupBy(item => item.Term).Select(item => item.Key).ToList();
            viewmodel.ListTerm = new ObservableCollection<string>(temp);

            temp = listSubject.GroupBy(item => item.Course).Select(item => item.Key).ToList();
            viewmodel.ListCourse = new ObservableCollection<string>(temp);

            temp = listSubject.GroupBy(item => item.Subject).Select(item => item.Key).ToList();
            viewmodel.ListSubjectName = new ObservableCollection<string>(temp);



            IoC.Application.GoToPage(ApplicationPage.ListSubject, viewmodel);
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
