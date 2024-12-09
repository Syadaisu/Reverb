CREATE TABLE [dbo].[Channels] (
    [Id]           INT           IDENTITY (1, 1) NOT NULL,
    [Name]         NVARCHAR (32) NOT NULL,
    [ServerId]     INT           NOT NULL,
    [RoleAccessId] INT           NOT NULL,
    [Description]  NVARCHAR (64) NULL,
    CONSTRAINT [PK_Channels] PRIMARY KEY CLUSTERED ([Id] ASC),
    CONSTRAINT [FK_Channels_Servers_ServerId] FOREIGN KEY ([ServerId]) REFERENCES [dbo].[Servers] ([Id])
);


GO
CREATE NONCLUSTERED INDEX [IX_Channels_ServerId]
    ON [dbo].[Channels]([ServerId] ASC);

