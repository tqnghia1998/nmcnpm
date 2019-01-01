using DbModel;
using System;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace AdminClient
{
    public class SubjectItemViewModel: CreateSubjectPageViewModel
    {
        #region Private Member

        private ListSubjectViewModel mContainer;


        #endregion

        #region Public Properties

        /// <summary>
        /// A flag to ignore delete command when it is running
        /// </summary>
        public bool DeleteIsRunning { get; set; }

        /// <summary>
        /// A flag to ignore click command when it is running
        /// </summary>
        public bool CommandIsRunning { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public bool UpdateIsRunning { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public bool IsLoaded { get; set; }

        /// <summary>
        /// Choose ?
        /// </summary>
        public bool IsChosen { get; set; }

        /// <summary>
        /// Amount of student
        /// </summary>
        public int TotalStudent { get; set; }

        #endregion

        #region Public Commands

        /// <summary>
        /// Delete command to delete a subject
        /// </summary>
        public ICommand DeleteCommand { get; set; }

        /// <summary>
        /// Click command to show credentials of this subject
        /// </summary>
        public ICommand ClickCommand { get; set; }

        /// <summary>
        /// Update credential of this subject
        /// </summary>
        public ICommand UpdateCommand { get; set; }

        #endregion

        #region Constructor

        /// <summary>
        /// 
        /// </summary>
        /// <param name="container"></param>
        public SubjectItemViewModel(ListSubjectViewModel container)
        {
            mContainer = container;
            ClickCommand = new RelayCommand(async () => await Click());
            UpdateCommand = new RelayParameterizedCommand(async (parameter) => await Update(parameter));
            DeleteCommand = new RelayCommand(async () => await DeleteAsync());
        }

        /// <summary>
        /// Default constructor
        /// </summary>
        public SubjectItemViewModel() : base()
        {
            ClickCommand = new RelayCommand(async () => await Click());
            UpdateCommand = new RelayParameterizedCommand(async (parameter) => await Update(parameter));
            DeleteCommand = new RelayCommand(async () => await DeleteAsync());
        }


        #endregion

        #region Command Methods

        /// <summary>
        /// Click method for ClickCommand
        /// </summary>
        public async Task Click()
        {
            await RunCommand(() => this.CommandIsRunning, async () =>
            {
                if (IsLoaded)
                {
                    CommandIsRunning = false;
                    IsChosen ^= true;
                    return;
                }
                else
                {
                    WebRequestResult<CreateSubjectCredentialsDataModel> response;

                    try
                    {
                        response = await WebRequests.GetAsync<CreateSubjectCredentialsDataModel>($"http://localhost:51197/api/subject/{ID.EditText}");
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show(ex.Message, "Get subjet failed", MessageBoxButton.OK, MessageBoxImage.Error);
                        CommandIsRunning = false;
                        return;
                    }


                    CreateSubjectCredentialsDataModel result = response.ServerResponse;

                    Credit = new TextEntryViewModel() { Label = "Credit", EditText = result.Subject.Credit.ToString() };
                    DateStart = result.Subject.TimeStart;
                    DateFinish = result.Subject.TimeFinish;

                    foreach (var item in result.Schedule)
                    {
                        var temp = SpecificTimeItems.Items.Where(e => e.DayInTheWeek.Equals(item.DayInTheWeek)).FirstOrDefault();
                        temp.IsChecked = true;
                        temp.Period = item.Period;
                        temp.Room = item.Room;
                        temp.TimeStart = item.TimeStart;
                        temp.TimeFinish = item.TimeFinish;
                    }

                    IsChosen ^= true;
                    IsLoaded = true;
                }
            });
        }

        /// <summary>
        /// Update method for UpdateCommand
        /// </summary>
        /// <returns></returns>
        public async Task Update(object parameter)
        {
            await RunCommand(() => this.UpdateIsRunning, async () =>
               {
                   CreateSubjectCredentialsDataModel DataToPost = ConvertToCredentailsCreate();

                   if (DataToPost == null)
                   {
                       return;
                   }

                   var respone = await WebRequests.PostAsync<ApiResponse<int>>("http://localhost:51197/api/subject/updatesubject", DataToPost);

                   if (respone.DisplayErrorIfFailed("Update subject failed"))
                   {
                       return;
                   }

                   MessageBox.Show("Update subject succeed", "Notify", MessageBoxButton.OK);
               });
        }

        public async Task DeleteAsync()
        {
            await RunCommand(() => this.DeleteIsRunning, async () =>
            {
                DateTime dateStart;
                
                try
                {
                    dateStart = DateTime.ParseExact(DateStart, "MM/dd/yyyy", CultureInfo.CurrentCulture);
                }
                catch(Exception ex)
                {
                    MessageBox.Show(ex.Message, "Delete failed", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }

                if (DateTimeOffset.UtcNow.Date >= dateStart)
                {
                    MessageBox.Show("Cannot delete a subject which had been started !", "Delete Failed",
                      MessageBoxButton.OK, MessageBoxImage.Error);

                    return;
                }


                var response = await WebRequests.PostAsync<ApiResponse<string>>("http://localhost:51197/api/subject/delete", ID.EditText);

                if (response.DisplayErrorIfFailed("Delete failed"))
                {
                    return;
                }

                MessageBox.Show(response.ServerResponse.Response, "Notify", MessageBoxButton.OK);
                mContainer.DeleteSubject(this);

            });

            
        }

        #endregion
    }
}