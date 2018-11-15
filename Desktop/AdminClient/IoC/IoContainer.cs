using DbModel;
using Ninject;

namespace AdminClient
{
    /// <summary>
    /// The IoC container for our application
    /// </summary>
    public static class IoC
    {
        #region Public Properties

        /// <summary>
        /// The kernel for the IoC container
        /// </summary>
        public static IKernel Kernel { get; private set; } = new StandardKernel();

        /// <summary>
        /// A shortcut to access the <see cref="ApplicationViewModel"/>
        /// </summary>
        public static ApplicationViewModel Application => IoC.Get<ApplicationViewModel>();



        /// <summary>
        /// A shortcut to access the <see cref="LoginCredentialsDataModel"/>
        public static ClientDataStore ClientDataStore => IoC.Get<ClientDataStore>();

        /// <summary>
        /// A shortcut to access the <see cref="SideMenuViewModel"/>
        public static SideMenuViewModel SideMenu => IoC.Get<SideMenuViewModel>();

        #endregion

        #region Constructor

        /// <summary>
        /// Sets up the IoC container, binds all information required and is ready for use
        /// NOTE: Must be call as soon as your application starts up to ensure all 
        ///       services can be found
        /// </summary>
        public static void Setup()
        {
            // Bind all required view models
            BindViewModels();

        }

        /// <summary>
        /// Binds all singleton view models
        /// </summary>
        private static void BindViewModels()
        {
            // Bind a single instance of the application view model
            Kernel.Bind<ApplicationViewModel>().ToConstant(new ApplicationViewModel());

            // Bind a single instance of the login credentials data model
            Kernel.Bind<ClientDataStore>().ToConstant(new ClientDataStore());

            // Bind a single instance of the side menu view model
            Kernel.Bind<SideMenuViewModel>().ToConstant(new SideMenuViewModel());
        }

        #endregion

        /// <summary>
        /// Get's a service from a IoC, of the specified type
        /// </summary>
        /// <typeparam name="T">The type to get</typeparam>
        /// <returns></returns>
        public static T Get<T>()
        {
            return Kernel.Get<T>();
        }
    }
}
