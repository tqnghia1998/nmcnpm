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
                Icon = "\uf022",
                Content = "List subject"
            });

            Items.Add(new SideMenuItemViewModel(OpenCreateSubjectPage)
            {
                Icon = "\uf044",
                Content = "Create subject"
            });

            Items.Add(new SideMenuItemViewModel(async () => await OpenStatisticPageAsync())
            {
                Icon = "\uf0ce",
                Content = "Statistic"
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

            viewmodel.ListMajor.Insert(0, "Major");
            viewmodel.ListTerm.Insert(0, "Term");
            viewmodel.ListCourse.Insert(0, "Course");

            List<SubjectItemViewModel> list = listSubject.Select(item => new SubjectItemViewModel(viewmodel)
            {
                Subject =  new TextEntryViewModel { Label = "Subject", EditText = item.Subject},
                ID = new TextEntryViewModel { Label = "Id", EditText = item.Id },
                Teacher = new TextEntryViewModel { Label = "Teacher", EditText = item.Teacher },
                Major = new TextEntryViewModel { Label = "Major", EditText = item.Major},
                Term = item.Term,
                Course = item.Course,
            }).ToList();

            viewmodel.ListSubject = new ObservableCollection<SubjectItemViewModel>(list);



            IoC.Application.GoToPage(ApplicationPage.ListSubject, viewmodel);
        }

        /// <summary>
        /// Go to statistic page
        /// </summary>
        /// <returns></returns>
        public async Task OpenStatisticPageAsync()
        {
            // Get data from server
            var reponse = await WebRequests.GetAsync<ApiResponse<StatisticResultApiModel>>("http://localhost:51197/api/subject/statistic");

            if (reponse.DisplayErrorIfFailed("Statistuc failed"))
            {
                //done
                return;
            }

            var data = reponse.ServerResponse.Response;
            StatisticViewModel viewmodel = new StatisticViewModel();

            // Create list major
            viewmodel.ListMajor = new ObservableCollection<string>(data.ListSubject.GroupBy(item => item.Major).Select(item => item.Key).ToList());
            viewmodel.ListMajor.Insert(0, "Major");

            // Create list subject
            var temp = data.ListSubject.Select(item => new SubjectItemViewModel
            
            {
                Major = new TextEntryViewModel { EditText = item.Major},
                Subject = new TextEntryViewModel { EditText = item.Subject },
                TotalStudent = item.TotalStudent,
            }).ToList();

            viewmodel.ListSubject = new ObservableCollection<SubjectItemViewModel>(temp);

            // Create list sort text
            viewmodel.ListSort = new ObservableCollection<string> { "Sort", "Ascending", "Descending" };

            IoC.Application.GoToPage(ApplicationPage.Statistic, viewmodel);
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
