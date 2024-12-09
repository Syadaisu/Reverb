CREATE TABLE [dbo].[Servers] (
    [Id]          INT              IDENTITY (1, 1) NOT NULL,
    [Name]        NVARCHAR (32)    NOT NULL,
    [Description] NVARCHAR (128)   NULL,
    [IsPublic]    BIT              NOT NULL,
    [OwnerId]     NVARCHAR (450)   NOT NULL,
    [PictureId]   UNIQUEIDENTIFIER NULL,
    CONSTRAINT [PK_Servers] PRIMARY KEY CLUSTERED ([Id] ASC),
    CONSTRAINT [FK_Servers_Users_OwnerId] FOREIGN KEY ([OwnerId]) REFERENCES [dbo].[Userss] ([Id])
);


GO
CREATE UNIQUE NONCLUSTERED INDEX [IX_Server_Name]
    ON [dbo].[Servers]([Name] ASC);


GO
CREATE NONCLUSTERED INDEX [IX_Server_OwnerId]
    ON [dbo].[Servers]([OwnerId] ASC);

