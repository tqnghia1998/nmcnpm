using DbModel;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using ServerApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ServerApp
{
    public class ApplicationDbContext : DbContext
    {
        #region Public Tables

        public DbSet<SubjectDataModel> Subject { get; set; }

        #endregion

        #region Constructor
        
        /// <summary>
        /// Default Constructor
        /// </summary>
        /// <param name="option"></param>
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> option) : base(option)
        {

        }

        #endregion

        #region Configure Database

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
        }



        #endregion
    }
}
