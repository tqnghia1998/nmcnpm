using DbModel;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;

namespace ServerApp
{
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
        #region Public Tables

        /// <summary>
        /// Bảng môn học
        /// </summary>
        public DbSet<SubjectDataModel> Subject { get; set; }

        /// <summary>
        /// Bảng thời khóa biểu của từng môn học
        /// </summary>
        public DbSet<ScheduleDataModel> Schedules { get; set; }

        /// <summary>
        /// Bảng danh sách đăng ký
        /// </summary>
        public DbSet<RegisteredDataModel> Registered { get; set; }

        /// <summary>
        /// Bảng các bài tập
        /// </summary>
        public DbSet<ExerciseDataModel> Exercise { get; set; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default Constructor
        /// </summary>
        /// <param name="option"></param>
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> option) : base(option)
        {

        }

        public ApplicationDbContext()
        {
        }

        #endregion

        #region Configure Database

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Set primary key cho bảng thời khóa biểu của từng môn học
            modelBuilder.Entity<ScheduleDataModel>().HasKey(c => new { c.Id, c.DayInTheWeek });

            // Set primary key cho bảng đăng ký môn học
            modelBuilder.Entity<RegisteredDataModel>().HasKey(c => new { c.Mssv, c.Id, c.DayInTheWeek });

            // Set primary key cho bảng bài tập
            modelBuilder.Entity<ExerciseDataModel>().HasKey(c => new { c.Mssv, c.Id });
            modelBuilder.Entity<ExerciseDataModel>().HasIndex(c => c.Name).IsUnique();
        }

        #endregion

        public List<CreateSubjectCredentialsDataModel> getSubjectAndSchedule(int i)
        {
            List<CreateSubjectCredentialsDataModel> list = new List<CreateSubjectCredentialsDataModel>();
            List<SubjectDataModel> listSubject = Subject.ToList();
            List<ScheduleDataModel> listSchedule = Schedules.ToList();
            for (int j = 0; j < listSubject.Count; j++)
            {
                list.Add(new CreateSubjectCredentialsDataModel(listSubject[j], listSchedule[j]));
            }
            return list;
        }
    }
}
